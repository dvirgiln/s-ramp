/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.sramp.shell.commands.archive;

import org.overlord.sramp.atom.archive.SrampArchive;
import org.overlord.sramp.shell.AbstractShellContextVariableLifecycleHandler;


/**
 * Creates a new, empty s-ramp batch archive.
 *
 * @author eric.wittmann@redhat.com
 */
public class NewArchiveCommand extends AbstractArchiveCommand {

	/**
	 * Constructor.
	 */
	public NewArchiveCommand() {
	}

	/**
	 * @see org.overlord.sramp.shell.api.shell.ShellCommand#execute()
	 */
	@Override
	public boolean execute() throws Exception {
        super.initialize();
        if (!validate()) {
            return false;
        }

		archive = new SrampArchive();
		getContext().setVariable(varName, archive, new AbstractShellContextVariableLifecycleHandler() {
			@Override
			public void onRemove(Object object) {
				SrampArchive.closeQuietly((SrampArchive) object);
			}
			@Override
			public void onContextDestroyed(Object object) {
				SrampArchive.closeQuietly((SrampArchive) object);
			}
		});
        print(messages.format("NewArchive.Opened")); //$NON-NLS-1$
        return true;
	}

    @Override
    protected boolean validate(String... args) {
        if (archive != null) {
            print(messages.format("NewArchive.AlreadyOpen")); //$NON-NLS-1$
            return false;
        }
        return true;
    }

}
