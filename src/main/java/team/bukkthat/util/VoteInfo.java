package team.bukkthat.util;

public class VoteInfo {

	String name = "";
	int votes;
	int posts;
	int plugins;
	
	public VoteInfo(String s, int i, int j, int k) {
		name = s;
		votes = i;
		posts = j;
		plugins = k;
	}
	
	public String getName() {
		return name;
	}
	public int getVotes() {
		return votes;
	}
	public int getPosts() {
		return posts;
	}
	public int getPlugins() {
		return plugins;
	}
	
}
