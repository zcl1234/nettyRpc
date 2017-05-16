package com.zcl.nettyRpc.parrallel;

/**
 * Created by zhangchenlin on 17/5/16.
 */
public enum BlockingQueueType {

    LINKED_BLOCKING_QUEUE("LinkedBlockingQueue"),
    ARRAY_BLOCKING_QUEUE("ArrayBlockingQueue"),
    SYNCHRONOUS_QUEUE("SynchronousQueue");

    private String value;
    private BlockingQueueType(String value)
    {
        this.value=value;
    }

    public static BlockingQueueType fromString(String value)
    {
        for(BlockingQueueType type:BlockingQueueType.values())
        {
            if(type.getValue().equalsIgnoreCase(value.trim()))
            {
                return type;
            }
        }
        throw new IllegalArgumentException("misMatch type with value :{}"+value);
    }

    public String getValue()
    {
        return value;
    }
}
