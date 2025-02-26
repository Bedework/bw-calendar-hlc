package org.bedework.hlc.admin;

import org.bedework.hlc.rw.ReadWriteHighLevelClient;

/** Provides high level operations on the calendar store.
 * The client may be operating in guest mode or as an
 * authenticated user.
 */
public interface AdminHighLevelClient extends
        ReadWriteHighLevelClient {
}
