package fi.oulu.danielszabo.pepper.screens.conv_demo.listeners;

import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.util.ArrayList;
import java.util.List;

import fi.oulu.danielszabo.pepper.tools.SimpleController;

public class ApproachingHumanGreeter extends CustomPepperEventListener implements HumanAwareness.OnHumansAroundChangedListener {

//    last time we checked, these humans were around. Initially no one
    private List<Human> lastHumansAroundList = new ArrayList<>();

    @Override
    public void onHumansAroundChanged(List<Human> humans) {

        runWithTimeout(() -> {
//            first person arrives to pepper
            if(lastHumansAroundList.size() == 0 && humans.size() == 1){
                SimpleController.say("Hi! Welcome to our faculty!;Hi there! Welcome to UBICOMP.;Hey there, welcome to ITEE!");
//            one more person joins
            } else if(lastHumansAroundList.size() > 0 && humans.size() > lastHumansAroundList.size()){
                SimpleController.say("Hi!");
            }
//            someone leaves
//            else if(humans.size() < lastHumansAroundList.size() && humans.size() > 0){
//                SimpleController.say("Bye!");
//            }
            //            everyone leaves
//            else if(humans.size() < lastHumansAroundList.size() ){
//                SimpleController.say("Bye! It was nice to see you!");
//            }
        }, 30_000);

        lastHumansAroundList = humans;
    }

}
