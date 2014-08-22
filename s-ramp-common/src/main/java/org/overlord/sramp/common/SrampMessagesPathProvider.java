package org.overlord.sramp.common;

import java.util.ArrayList;
import java.util.List;

import org.overlord.commons.i18n.MessagesPathProvider;

public class SrampMessagesPathProvider implements MessagesPathProvider {

    @Override
    public List<String> getMessagesPaths() {
        List<String> paths = new ArrayList<String>();
        paths.add("org.overlord.sramp.ui.server.i18n");
        paths.add("org.overlord.sramp.common.i18n");
        paths.add("org.overlord.sramp.repository.i18n");
        paths.add("org.overlord.sramp.server.i18n");
        paths.add("org.overlord.sramp.repository.jcr.modeshape.i18n");
        paths.add("org.overlord.sramp.atom.i18n");
        paths.add("org.overlord.sramp.client.i18n");
        paths.add("org.overlord.sramp.integration.switchyard.i18n");
        paths.add("org.overlord.sramp.integration.teiid.i18n");
        paths.add("org.overlord.sramp.repository.jcr.i18n");
        paths.add("org.overlord.sramp.shell.i18n");
        paths.add("org.overlord.sramp.shell.api.i18n");
        paths.add("org.overlord.sramp.wagon.i18n");
        return paths;
    }

}
