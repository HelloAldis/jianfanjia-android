package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class DesignerCanOrderList extends BaseModel {
    private List<Designer> rec_designer;
    private List<Designer> favorite_designer;

    public List<Designer> getRec_designer() {
        return rec_designer;
    }

    public void setRec_designer(List<Designer> rec_designer) {
        this.rec_designer = rec_designer;
    }

    public List<Designer> getFavorite_designer() {
        return favorite_designer;
    }

    public void setFavorite_designer(List<Designer> favorite_designer) {
        this.favorite_designer = favorite_designer;
    }
}
