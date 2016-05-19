package com.jianfanjia.api.model;

import java.util.List;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-19 15:18
 */
public class TeamList {

    private List<Team> teams;

    private int total;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
