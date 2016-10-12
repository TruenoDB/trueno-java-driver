package org.trueno.driver.lib.core.data_structures;

import org.jdeferred.Promise;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trueno.driver.lib.core.communication.Message;

/**
 * <b>Edge Class</b>
 * <p>TruenoDB Edge primitive data structure class.</p>
 *
 * @author Victor Santos
 * @author Miguel Rivera
 * @version 0.1.0
 */
public class Edge extends Component {

    private final Logger log = LoggerFactory.getLogger(Edge.class.getSimpleName());

    /**
     * Initializes a new Edge
     */
    public Edge() {
        this.put("source", "");
        this.put("target", "");
        this.put("partition", "");

        this.setType("e");

        log.trace("Edge object created");
    }

    /**
     * Returns the source of this Edge
     *
     * @return Edge source
     */
    public String getSource() {
        return this.get("source").toString();
    }

    /**
     * Sets the source of this Edge
     *
     * @param source
     *         new Edge source
     */
    public void setSource(String source) {
        this.put("source", source);
    }

    /**
     * Returns whether the source of this Edge is set
     *
     * @return true if source is set, false otherwise.
     */
    boolean hasSource() {
        return this.has("source") && !this.get("source").toString().isEmpty();
    }

    /**
     * Returns the target of this Edge
     *
     * @return Edge target
     */
    public String getTarget() {
        return this.get("target").toString();
    }

    /**
     * Sets the target of this Edge
     *
     * @param target
     *         new Edge target
     */
    public void setTarget(String target) {
        this.put("source", target);
    }

    /**
     * Returns whether the target of this Edge is set
     *
     * @return true if targe is set, false otherwise.
     */
    boolean hasTarget() {
        return this.has("target") && !this.get("target").toString().isEmpty();
    }

    /**
     * Returns the partition of this Edge
     *
     * @return Edge partition
     */
    public String getPartition() {
        return this.get("partition").toString();
    }

    /**
     * Sets the partition of this Edge
     *
     * @param partition
     *         new Edge partition
     */
    public void setPartition(String partition) {
        this.put("partition", partition);
    }

    /**
     * Returns a new filter to be applied on this Edge
     *
     * @return new Edge filter
     */
    public Filter filter() {
        return new Filter();
    }

    /**
     * Returns the vertices connected to this Edge
     *
     * @return Promise with the vertices search result.
     */
    public Promise<JSONObject, JSONObject, Integer> vertices() {
        final String apiFun = "ex_vertices";

        if (!this.validateGraphLabel())
            return null;

        if (!this.hasId()) {
            log.error("Edge id is required, set this edge instance id or load edge.");
            return null;
        }

        Message msg = new Message();
        JSONObject payload = new JSONObject();

        payload.put("graph", this.getParentGraph().getLabel());
        payload.put("id", this.getId());

        msg.setPayload(payload);

        log.debug("{} – {}", apiFun, msg.toString());

        return this.getParentGraph().getConn().call(apiFun, msg);
    }
}
