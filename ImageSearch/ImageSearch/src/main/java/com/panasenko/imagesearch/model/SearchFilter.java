/**
 * File: SearchFilter.java
 * Created: 1/24/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch.model;

import java.io.Serializable;

/**
 * SearchFilter
 * Data structure for storing search filter options.
 */
public class SearchFilter implements Serializable {

    private static final long serialVersionUID = 4023948209358234L;

    private String imageSize;
    private String imageColor;
    private String imageType;
    private String siteFilter;

    /**
     * Default constructor.
     */
    public SearchFilter() {
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageColor() {
        return imageColor;
    }

    public void setImageColor(String imageColor) {
        this.imageColor = imageColor;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }
}
