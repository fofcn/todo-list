package com.epam.todo.guid.model;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "worker_node")
public class WorkerNode extends com.xfvape.uid.worker.entity.WorkerNodeEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "HOST_NAME", nullable = false)
    private String hostName;

    @Column(name = "PORT", nullable = false)
    private String port;

    @Column(name = "TYPE", nullable = false)
    private int type;

    @Column(name = "LAUNCH_DATE", nullable = false)
    private Date launchDate = new Date();

    @CreatedDate
    @Column(name = "CREATED", nullable = false)
    private Date created;

    @LastModifiedDate
    @Column(name = "MODIFIED", nullable = false)
    private Date modified;
}