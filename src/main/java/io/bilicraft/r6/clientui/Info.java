package io.bilicraft.r6.clientui;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;

public class Info {
    private int id;
    private ArrayList<String> texture = Lists.newArrayList();
    private String name;
    private String localName;
    private String ownerid;

    public Info(int id, String name, String owner, String textureName) {
	this.id = id;
	this.name = name;
	this.ownerid = owner;
	this.localName = I18n.format(ownerid+":"+name);
	if (!textureName.equals("")) {
	    this.texture.add(translatePath(textureName, ownerid));
	}
    }

    private String translatePath(String name) {
	return translatePath(name, this.ownerid);
    }

    private String translatePath(String texture, String owner) {
	return id > 4096 ? "assets/"+ owner+"/textures/items/"+texture+".png":"assets/"+ owner+"/textures/blocks/"+texture+".png";
		
    }
    
    public void addTexture(String texture) {
	this.texture.add(translatePath(texture));
    }

    public String getName() {
	return name;
    }

    public int getId() {
	return id;
    }

    public String getOwnerid() {
	return ownerid;
    }

    @Override
    public String toString() {
	return id + ":" + name + ":" + ownerid + ":" + texture+":"+localName;
    }
}
