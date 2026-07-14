package gabriel.hb.MyLifeBackend.resources.dto;

import java.io.Serializable;

public class ProcessReadingsRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String themeName;
    private String rawText;

    public ProcessReadingsRequest() {}

    public String getThemeName() { 
    	return themeName; 
    }
    
    public void setThemeName(String themeName) { 
    	this.themeName = themeName; 
    }

    public String getRawText() { 
    	return rawText;
    }
    
    public void setRawText(String rawText) { 
    	this.rawText = rawText; 
    }
}