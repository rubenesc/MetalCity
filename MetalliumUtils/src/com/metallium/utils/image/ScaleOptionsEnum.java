/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.image;

/**
 * 20110507
 * @author Ruben
 */
public enum ScaleOptionsEnum {

    SCALE,  //Just scales the image
    SCALE_CROP_SQUARE, //Scales and Crops the image as a square, (same width and height determined by the width parameter)
    SCALE_CROP;   //Scales and Crops the image as a square, (with the width and height specified)
}
