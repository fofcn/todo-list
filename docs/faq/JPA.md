# 1. Unable to locate Attribute with the given name - Spring Data JPA Projections, Hibernate and BaseEntity
原因：
JPA的基类定义没有增加@MappedSuperclass注解导致基类定义的字段报错
```java
@Data
@EntityListeners(value = AuditingEntityListener.class)
public class BaseJpaEntity {

    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "last_modified_time", nullable = false)
    private LocalDateTime lastModifiedTime;
}
```

解决方案：
1. 在JPA定义的基类中增加@MappedSuperclass注解
```java
@Data
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public class BaseJpaEntity {

    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "last_modified_time", nullable = false)
    private LocalDateTime lastModifiedTime;
}
 
```