/*
 * jVerbs: RDMA verbs support for the Java Virtual Machine
 *
 * Author: Patrick Stuedi <stu@zurich.ibm.com>
 *
 * Copyright (C) 2016, IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ibm.disni.rdma.verbs.impl;

import java.io.IOException;

import com.ibm.disni.rdma.verbs.IbvMr;
import com.ibm.disni.rdma.verbs.SVCDeregMr;


public class NatDeregMrCall extends SVCDeregMr {
	private NativeDispatcher nativeDispatcher;
	private RdmaVerbsNat verbs;
	
	private NatIbvMr mr;
	private boolean valid;

	public NatDeregMrCall(RdmaVerbsNat verbs, NativeDispatcher nativeDispatcher) {
		this.verbs = verbs;
		this.nativeDispatcher = nativeDispatcher;
		this.valid = false;
	}

	public void set(IbvMr mr) {
		this.mr = (NatIbvMr) mr;
		this.valid = true;
	}

	public SVCDeregMr execute() throws IOException {
		int ret = nativeDispatcher._deregMr(mr.getObjId());
		if (ret != 0){
			throw new IOException("Memory de-registration failed, ret " + ret);
		}
		return this;
	}
	
	public boolean isValid() {
		return valid;
	}

	public SVCDeregMr free() {
		verbs.free(this);
		this.valid = false;
		return this;
	}
}
