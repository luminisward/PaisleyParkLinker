package com.example.messagingstompwebsocket;

import org.springframework.stereotype.Component;

@Component
public class Connections {
    private Integer count;

    //创建 SingleObject 的一个对象
    private static Connections instance = new Connections();


    private Connections() {
        this.count = 0;
    }

    public Connections getInstance() {
        return instance;
    }

    public Integer getCount() {
        return count;
    }

    public void increase() {
        this.count++;
    }

    public void decrease() {
        this.count--;
    }
}
