/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2010, Wolfgang Puffitsch <wpuffits@mail.tuwien.ac.at>

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package cruiser.common;

public class WireControlMessage extends WireMessage {

	// value * 10000
	private final short value;

	public WireControlMessage(WireMessage.Type type, short value) {
		super(type);
		this.value = value;
	}

	public String toString() {
		return buildMessage(getType(), value);
	}

	public short getValue() {
		return value;
	}

	public static WireControlMessage fromString(String msg) {
		if (!WireMessage.checkMessage(msg)) {
			return null;
		}
		Type t = WireMessage.parseType(msg);
		return new WireControlMessage(t, (short)WireMessage.parseData(msg, t.length));
	}
}