package com.epam.todo.task.domain.valueobject;

import com.epam.common.core.lang.Assert;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

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
        Assert.isNotEmpty(title);
        Assert.checkBetween(title, 1, 20);
        Assert.isNotEmpty(subTitle);
        Assert.checkBetween(subTitle, 1, 200);
    }
}
