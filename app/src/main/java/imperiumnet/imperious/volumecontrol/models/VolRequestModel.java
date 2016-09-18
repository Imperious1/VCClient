package imperiumnet.imperious.volumecontrol.models;

/**
 * Created by blaze on 8/22/2016.
 */
public class VolRequestModel {
    private int req, increment;

    public int getReq() {
        return req;
    }

    public VolRequestModel setReq(int req) {
        this.req = req;
        return this;
    }

    public int getIncrement() {
        return increment;
    }

    public VolRequestModel setIncrement(int increment) {
        this.increment = increment;
        return this;
    }
}
