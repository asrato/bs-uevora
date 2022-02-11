package rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "participant")
public class Participant {
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private int dorsal;
    @XmlElement(required = true)
    private String genre;
    @XmlElement(required = true)
    private String eventName;
    @XmlElement(required = true)
    private String tier;
    @XmlElement(required = true)
    private int chipID;

    public Participant() {
        this.name = "";
        this.dorsal = 0;
        this.genre = "";
        this.eventName = "";
        this.tier = "";
        this.chipID = 0;
    }

    public Participant(String name, int dorsal, String genre, String eventName, String tier, int chipID) {
        this.name = name;
        this.dorsal = dorsal;
        this.genre = genre;
        this.eventName = eventName;
        this.tier = tier;
        this.chipID = chipID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public int getChipID() {
        return chipID;
    }

    public void setChipID(int chipID) {
        this.chipID = chipID;
    }
}
