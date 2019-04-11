/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.transport;

import com.bunjlabs.fuga.channel.Channel;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public interface Transport<T> extends Channel<T> {

    void enable() throws TransportException;
}
