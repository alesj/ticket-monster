/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.example.ticketmonster.client.categories;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.protocols.MessageParts;
import org.jboss.errai.workspaces.client.api.ProvisioningCallback;
import org.jboss.errai.workspaces.client.api.WidgetProvider;
import org.jboss.errai.workspaces.client.api.annotations.LoadTool;
import org.jboss.seam.example.ticketmonster.model.EventCategory;

import java.util.List;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Apr 6, 2010
 */
@LoadTool(name = "Admin", group="Categories")
public class CategoryAdmin implements WidgetProvider, MessageCallback
{
    private final MessageBus bus = ErraiBus.get();

    private HTML responsePanel;

    public void provideWidget(ProvisioningCallback callback)
    {

        bus.subscribe("CategoryAdmin", this);
        
        LayoutPanel panel = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.VERTICAL));

        Button button = new Button("Load categories", new ClickHandler()
        {
            public void onClick(ClickEvent clickEvent)
            {

                MessageBuilder.createMessage()
                        .toSubject("CategorySearch")
                        .signalling()
                        .with(MessageParts.ReplyTo, "CategoryAdmin")
                        .done().sendNowWith(bus);

            }
        });

        responsePanel = new HTML();

        panel.add(button);
        panel.add(responsePanel);

        callback.onSuccess(panel);
    }

    public void callback(Message message) {
        
        List<EventCategory> values = message.get(List.class, "categories");

        StringBuffer sb = new StringBuffer();
        sb.append("<ul>");
        for(EventCategory a : values)
            sb.append("<li>").append(a.getDescription());
        sb.append("</ul>");

        responsePanel.setHTML(sb.toString());
    }
}

