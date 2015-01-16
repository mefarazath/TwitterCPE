package org.wso2.uima.cpe.reader.data;

import java.util.Date;

/**
 * Created by farazath on 1/5/15.
 */
public class Tweet {

    private String text;
    private Date createdAt;
    private long id;

   
	public Tweet(Long id,Date createdAt, String text) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }

	public long getId() {
		return id;
	}
	
    public String getText() {
        return text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return text+"  ["+createdAt.toString()+"]   id: "+id;
    }

}
