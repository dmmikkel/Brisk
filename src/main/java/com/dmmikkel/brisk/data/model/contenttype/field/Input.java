package com.dmmikkel.brisk.data.model.contenttype.field;

import java.util.List;
import java.util.Map;

public class Input
{
    private String             name;
    private String             displayName;
    private String             type;
    private String             value; // if type = string
    private List<Input>        values; // if type = array
    private Map<String, Input> children; // if type = object
}
