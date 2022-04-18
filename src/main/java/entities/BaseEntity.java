package entities;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
    protected static final long serialVersionUID = 1L;
    
    public abstract Integer getId();
    public abstract void setId(Integer id);
}
