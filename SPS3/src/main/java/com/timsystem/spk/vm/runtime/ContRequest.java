package com.timsystem.spk.vm.runtime;

import java.util.Objects;

public class ContRequest {
    private Container headCont;
    private String request;

    public ContRequest(Container headCont, String request) {
        this.headCont = headCont;
        this.request = request;
    }
    public Container get(){
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < request.length(); i++){
            char cur = request.charAt(i);
            if(cur == ']'){
                if (!Objects.equals(headCont.getName(), buffer.toString())) {
                    if(headCont instanceof Klass klass_c){
                        headCont = klass_c.get(buffer.toString());
                    }
                    else if(headCont instanceof Function func){
                        headCont = func;
                    }
                }
                buffer = new StringBuilder();
            }
            else if(cur != '[') buffer.append(cur);
        }
        return headCont;
    }
}
