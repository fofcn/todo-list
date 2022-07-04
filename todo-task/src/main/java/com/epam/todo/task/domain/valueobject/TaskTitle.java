package com.epam.todo.task.domain.valueobject;

import com.epam.common.core.lang.Assert;
import lombok.Getter;

@Getter
public class TaskTitle {

    private String title;

    private String subTitle;

    public TaskTitle() {

    }

    public TaskTitle(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public void isValid() {
        // we should put technical validations (such as check length,correct input formats,correct data type,...)
        // out of domain model and somewhere like application layer to keep domain object clear.
        Assert.isNotEmpty(title);
        Assert.checkBetween(title, 1, 20);
        Assert.isNotEmpty(subTitle);
        Assert.checkBetween(subTitle, 1, 200);

        // 其他业务规则，比如title里面要包含一个emoji笑脸
    }
}
