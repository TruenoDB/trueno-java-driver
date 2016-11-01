package org.trueno.driver.lib.core.data_structures;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trueno.driver.lib.core.communication.Message;
import org.trueno.driver.lib.core.utils.Pair;

/**
 * <b>Edge Class</b>
 * <p>TruenoDB Edge primitive data structure class.</p>
 *
 * @author Victor Santos
 * @author Miguel Rivera
 * @version 0.1.0
 */
public class Edge extends Component {

    /**
     * Initializes a new Edge
     */
    public Edge() {
        this.put("source", "");
        this.put("target", "");
        this.put("partition", "");

        this.setType(ComponentType.EDGE);
    }

    /**
     * Overloaded constructor. Allows to pass by parameter a Edge already instantiated.
     *
     * @param obj JSONObject with preset keys id, prop (properties), meta and comp (computed).
     * @param parent Graph that contains the edge.
     */
    public Edge(JSONObject obj, Graph parent) {
        super(obj);

        this.setSource(obj.get("source"));
        this.setTarget(obj.get("target"));
        this.setType(ComponentType.EDGE);
        this.setParentGraph(parent);
    }

    /**
     * Initializes a new Edge with source and target
     */
    public Edge(Object source, Object target) {
        this.put("source", source);
        this.put("target", target);
        this.put("partition", "");

        this.setType(ComponentType.EDGE);
    }

    /**
     * Returns the edges identifier
     *
     * @return the identifier based on a Pair class.
     */
    public Object getId() {

        return new Pair<>(this.getSource(), this.getTarget());
    }

    /**
     * Returns the source of this Edge
     *
     * @return Edge source
     */
    public Object getSource() {
        return this.get("source");
    }

    /**
     * Sets the source of this Edge
     *
     * @param source new Edge source
     */
    public void setSource(Object source) {
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
    public Object getTarget() {
        return this.get("target");
    }

    /**
     * Sets the target of this Edge
     *
     * @param target new Edge target
     */
    public void setTarget(Object target) {
        this.put("target", target);
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
     * @param partition new Edge partition
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
        final Deferred<JSONObject, JSONObject, Integer> deferred = new DeferredObject<>();
        Promise<JSONObject, JSONObject, Integer> promise = deferred.promise();

        if (!this.hasId()) {
            log.error("Edge id is required, set this edge instance id or load edge.");
            deferred.reject(new JSONObject().put("error", "Edge id is required, set this edge instance id or load edge."));
        }
        else if (!this.validateGraphLabel()) {
            log.error("{} – Graph label is empty", this.getId());
            deferred.reject(new JSONObject().put("error", this.getId() + " – Graph label is empty"));
        }
        else {
            Message msg = new Message();
            JSONObject payload = new JSONObject();

            payload.put("graph", this.getParentGraph().getLabel());
            payload.put("id", this.getId());

            msg.setPayload(payload);

            log.debug("{} – {}", apiFun, msg.toString());

            this.getParentGraph().getConn().call(apiFun, msg).then(result -> {
                deferred.resolve(new JSONObject().put("source", new Vertex((JSONObject)result.get("source"), this.getParentGraph()))
                        .put("target", new Vertex((JSONObject)result.get("target"), this.getParentGraph())));
            });
        }

        return promise;
    }
}
