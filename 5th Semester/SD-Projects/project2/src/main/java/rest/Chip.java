package rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="chip")
public class Chip {
    @XmlElement(required = true)
    private int chipID;
    @XmlElement(required = true)
    private String section;
    @XmlElement(required = true)
    private String time;

    public Chip() {
        this.chipID = 0;
        this.section = "";
        this.time = "";
    }

    public Chip(int chipID, String section, String time) {
        this.chipID = chipID;
        this.section = section;
        this.time = time;
    }

    public int getChipID() {
        return chipID;
    }

    public void setChipID(int chipID) {
        this.chipID = chipID;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
