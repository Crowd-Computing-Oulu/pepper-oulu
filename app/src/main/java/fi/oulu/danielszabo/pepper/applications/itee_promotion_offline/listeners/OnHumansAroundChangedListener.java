package fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.listeners;

import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.util.List;

public class OnHumansAroundChangedListener implements HumanAwareness.OnHumansAroundChangedListener {
    @Override
    public void onHumansAroundChanged(List<Human> humans) {
//        PepperApplication.simpleSay("I see " + humans.size() + " people.");
    }
}
