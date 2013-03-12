package team.bukkthat.util;

import lombok.Getter;

public class VoteInfo {

    @Getter
    private final String name;
    @Getter
    private final int votes;
    @Getter
    private final int posts;
    @Getter
    private final int plugins;

    public VoteInfo(String name, int votes, int posts, int plugins) {
        this.name = name;
        this.votes = votes;
        this.posts = posts;
        this.plugins = plugins;
    }
}