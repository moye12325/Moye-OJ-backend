package com.moye.oj.judge.codesandbox.impl;

import com.moye.oj.judge.codesandbox.CodeSandbox;
import com.moye.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.moye.oj.judge.codesandbox.model.ExecuteCodeResponse;

public class RemoteCodeSandbox implements CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        System.out.println("远程代码沙箱");
        return null;
    }
}
