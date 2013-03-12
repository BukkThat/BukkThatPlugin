package team.bukkthat;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;

public class MySQLConnectionPool implements Closeable {

    private class JBDCConnection {
        @Getter
        private final Connection connection;
        @Getter
        private int load = 0;

        private JBDCConnection(Connection connection) {
            this.connection = connection;
        }

        private void incrementLoad() {
            this.load++;
        }

        private void terminate() {
            try {
                this.connection.close();
            } catch (final SQLException e) {
                System.out.println("SQLException while terminating pool connection: " + e.getMessage());
            }
        }
    }

    private final static int poolSize = 4;
    private final JBDCConnection[] connections;
    private final String url;

    private final Lock lock = new ReentrantLock();

    public MySQLConnectionPool(String url) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.url = url;
        this.connections = new JBDCConnection[MySQLConnectionPool.poolSize];
        for (int i = 0; i < this.connections.length; i++) {
            this.connections[i] = new JBDCConnection(DriverManager.getConnection(url));
        }
    }

    @Override
    public void close() {
        this.lock.lock();
        for (final JBDCConnection connection : this.connections) {
            connection.terminate();
        }
        this.lock.unlock();
    }

    public Connection getConnection() throws SQLException {
        this.lock.lock();
        try {
            int lowestIndex = 0;
            int lowestLoad = Integer.MAX_VALUE;
            for (int i = 0; i < this.connections.length; i++) {
                final JBDCConnection connection = this.connections[i];
                if (!connection.getConnection().isValid(1)) {
                    connection.terminate();
                    this.connections[i] = new JBDCConnection(DriverManager.getConnection(this.url));
                }
                if (connection.getLoad() < lowestLoad) {
                    lowestLoad = connection.getLoad();
                    lowestIndex = i;
                }
            }

            this.connections[lowestIndex].incrementLoad();
            return this.connections[lowestIndex].getConnection();
        } finally {
            this.lock.unlock();
        }
    }
}