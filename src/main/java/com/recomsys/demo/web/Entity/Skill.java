package com.recomsys.demo.web.Entity;

import java.util.List;

public class Skill {
    private String wantjob;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private List<String> skillset;

    public String getWantjob() {
        return wantjob;
    }

    public void setWantjob(String wantjob) {
        this.wantjob = wantjob;
    }

    public List<String> getSkillset() {
        return skillset;
    }

    public void setSkillset(List<String> skillset) {
        this.skillset = skillset;
    }
}
