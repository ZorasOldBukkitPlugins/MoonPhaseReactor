/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lagopusempire.moonphasereactor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author MrZoraman
 */
@Entity
@Table(name = "mpr_blocks")
public class BlockData {

    @Id
    private int id;

    private int x;
    private int y;
    private int z;

    private String worldName;

    private String normal_block;
    private String special_block;

    private int moon_phase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getNormal_block() {
        return normal_block;
    }

    public void setNormal_block(String normal_block) {
        this.normal_block = normal_block;
    }

    public String getSpecial_block() {
        return special_block;
    }

    public void setSpecial_block(String special_block) {
        this.special_block = special_block;
    }

    public int getMoon_phase() {
        return moon_phase;
    }

    public void setMoon_phase(int moon_phase) {
        this.moon_phase = moon_phase;
    }

}
