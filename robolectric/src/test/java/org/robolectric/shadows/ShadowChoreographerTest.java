package org.robolectric.shadows;

import android.view.Choreographer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;
import org.robolectric.util.TimeUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(TestRunners.MultiApiSelfTest.class)
public class ShadowChoreographerTest {

  @Test
  public void setFrameInterval_shouldUpdateFrameInterval() {
    final long frameInterval = 10 * TimeUtils.NANOS_PER_MS;
    ShadowChoreographer.setFrameInterval(frameInterval);

    final Choreographer instance = ShadowChoreographer.getInstance();
    long time1 = instance.getFrameTimeNanos();
    long time2 = instance.getFrameTimeNanos();

    assertThat(time2 - time1).isEqualTo(frameInterval);
  }

  @Test
  public void removeFrameCallback_shouldRemoveCallback() {
    Choreographer instance = ShadowChoreographer.getInstance();
    Choreographer.FrameCallback callback = mock(Choreographer.FrameCallback.class);
    instance.postFrameCallbackDelayed(callback, 1000);
    instance.removeFrameCallback(callback);
    ShadowApplication.getInstance().getForegroundThreadScheduler().advanceToLastPostedRunnable();
    verify(callback, never()).doFrame(anyInt());
  }

  @Test
  public void reset_shouldResetFrameInterval() {
    ShadowChoreographer.setFrameInterval(1);
    assertThat(ShadowChoreographer.getFrameInterval()).isEqualTo(1);

    ShadowChoreographer.reset();
    assertThat(ShadowChoreographer.getFrameInterval()).isEqualTo(10 * TimeUtils.NANOS_PER_MS);
  }
}
