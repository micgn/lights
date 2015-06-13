/*
 * Fiducia IT AG, All rights reserved. Use is subject to license terms.
 */

package de.mign.restlights;

public interface StatusSubscriber {

    void say(String string);

    void error(String message);

}
