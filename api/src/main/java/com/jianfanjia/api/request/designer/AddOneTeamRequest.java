package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.model.Team;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.designer
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 17:13
 */
public class AddOneTeamRequest extends BaseRequest {

    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
