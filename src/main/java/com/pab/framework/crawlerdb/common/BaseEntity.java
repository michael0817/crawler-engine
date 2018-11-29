package com.pab.framework.crawlerdb.common;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class BaseEntity implements Serializable,Cloneable {
    private static  final long serialVersionUID=1L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString( this );
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode( this );
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals( this ,obj);
    }

}
