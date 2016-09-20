package Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by fdx on 2016/9/20.
 */
@XmlRootElement(name = "setting")
public class MySettings {
    @XmlElement
    private List<BroadCast> broadCasts;

    @XmlElement
    private List<PackageName> packageNames;

    public List<BroadCast> getBroadCasts() {
        return broadCasts;
    }

    public void setBroadCasts(List<BroadCast> broadCasts) {
        this.broadCasts = broadCasts;
    }
}
