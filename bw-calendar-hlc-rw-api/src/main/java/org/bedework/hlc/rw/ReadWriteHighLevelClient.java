package org.bedework.hlc.rw;

import org.bedework.hlc.ro.ReadOnlyHighLevelClient;

/** Provides high level operations on the calendar store.
 * The client may be operating in guest mode or as an
 * authenticated user.
 */
public interface ReadWriteHighLevelClient extends
        ReadOnlyHighLevelClient {
}
