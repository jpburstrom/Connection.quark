+SequenceableCollection {
	connectAll {
		|...dependants|
		^ConnectionList.newFrom(
			this.collect(_.connectTo(*dependants))
		)
	}

	connectEach {
		|signalNameOrFunc, dependantList, methodNameOrFunc|
		if (this.size != dependantList.size) {
			Error("connectEach requires collections of equal size (this.size = %, other.size = %)".format(this.size, dependantList.size)).throw;
		};

		if (signalNameOrFunc.notNil && signalNameOrFunc.isKindOf(Function).not) {
			var tempVal = signalNameOrFunc;
			signalNameOrFunc = { |o| o.signal(tempVal) };
		};

		if (methodNameOrFunc.notNil && methodNameOrFunc.isKindOf(Function).not) {
			var tempVal = methodNameOrFunc;
			methodNameOrFunc = { |o| o.methodSlot(tempVal) };
		};

		^this.collectAs({
			|object, i|
			var dependant = dependantList[i];

			if (methodNameOrFunc.notNil) {
				dependant = methodNameOrFunc.value(dependant);
			};

			if (signalNameOrFunc.notNil) {
				object = signalNameOrFunc.value(object);
			};

			object.connectTo(dependant);
		}, ConnectionList)
	}

	eachMethodSlot {
		|method|
		^this.collect(_.methodSlot(method));
	}

	eachValueSlot {
		|setter|
		^this.collect(_.valueSlot(setter));
	}

	eachInputSlot {
		^this.collect(_.inputSlot());
	}

	eachArgSlot {
		|argName|
		^this.collect(_.argSlot(argName));
	}
}