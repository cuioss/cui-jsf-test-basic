package de.icw.cui.test.jsf.support.beans;

import java.io.Serializable;

import javax.inject.Named;

@SuppressWarnings("javadoc")
@Named(NamedSimpleBean.BEAN_NAME)
public class NamedSimpleBean implements Serializable {

    private static final long serialVersionUID = 2891864631641853743L;

    public static final String BEAN_NAME = "namedSimpleBean";
}
