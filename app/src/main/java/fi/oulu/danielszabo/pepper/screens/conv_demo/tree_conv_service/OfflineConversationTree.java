package fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service;

import java.util.Arrays;

import fi.oulu.danielszabo.pepper.log.LOG;
import lombok.Getter;

@Getter
public class OfflineConversationTree {

    private final OfflineConversationTreeNode root;

    private OfflineConversationTreeNode current;

    public OfflineConversationTreeNode selectChild(int childIndex) {
//        this WILL throw index out of bounds exception
//        if we're going crazy selecting children mindlessly
        return current = current.getChildren()[childIndex];
    }

    public OfflineConversationTreeNode input(String input) {
//        let's find which child corresponds to this input
        for (int i = 0; i < current.getOptions().length; i++) {
//        this WILL throw index out of bounds exception
//        if we're going crazy selecting children mindlessly

//            let's check the input we received is the displayed option itself
            if (current.getOptions()[i].equalsIgnoreCase(input)) {
//                yay, it is!
                LOG.debug(this, "Matched option: " + current.getOptions()[i]);
                return current = current.getChildren()[i];
            } else {
//                well, it wasn't. Now let's check if the input is one of the phrases for this option
                for (int j = 0; j < current.getOptionPhraseSets()[i].length; j++) {
                    if (input.equalsIgnoreCase(current.getOptionPhraseSets()[i][j])) {
//                        yep!
                        LOG.debug(this, "Matched option: " + current.getOptions()[i]);
                        return current = current.getChildren()[i];
                    }
                }
            }
        }

        LOG.error(this,
                "No matched option for input : \"" +
                        input + "\" in node with options: \n" +
                        Arrays.toString(current.getOptions()));

//        we are already on a leaf or the input is not valid on this node.
//        let's return null and decide outside how we handle it
        return null;
    }


    //    Now let's build the whole tree.
    public OfflineConversationTree() {

//        LAYER 1
        current = root = new OfflineConversationTreeNode(
                "Hello, I am Pepper. How may I help?",
                new String[]{
                        "Hello",
                        "Tell me about yourself",
                        "Tell me about ITEE",
                        "Tell me about UBICOMP",
                        "What is Sona?"
                },
                new String[][]{
                        new String[]{
                                "Hi, Pepper", "Hello, Pepper", "Hey, Pepper", "Greetings, Pepper", "Good morning, Pepper", "Good afternoon, Pepper", "Good evening, Pepper", "Hi", "Hello there", "Nice to meet you", "Hey"
                        },
                        new String[]{
                                "Tell me about yourself", "What can you do?", "Who are you?", "Introduce yourself", "What are your capabilities", "What do you know?", "What is your purpose", "Yourself"
                        },
                        new String[]{
                                "Tell me about ITEE", "What is ITEE", "Can you give me information on ITEE?", "What does ITEE stand for", "What is the ITEE department about?", "I would like to know more about ITEE", "What are the research areas in ITEE?", "ITEE"
                        },
                        new String[]{
                                "Tell me about UBICOMP", "What is UBICOMP", "Give me information on UBICOMP", "UBICOMP"
                        },
                        new String[]{
                                "Tell me about Sona", "What is Sona", "Details on Sona please", "Sona", "Sona please"
                        }
                }
        );

        OfflineConversationTreeNode octn_0 = new OfflineConversationTreeNode(
                "Hello, nice to meet you.",
                new String[]{
                        "What can you do?",
                        "Who programmed you?",
                        "What is you purpose?"
                },
                new String[][]{
                        new String[]{
                                "What can you do", "Your capabilities", "Your functions", "Your abilities",
                        },
                        new String[]{
                                "Who programmed you", "Who created you?", "Who designed your algorithms", "Who wrote your code", "Who developed your intelligence"
                        },
                        new String[]{
                                "What is your purpose",
                                "What is the purpose of your existence",
                                "Reason for your being",
                                "What motivates you", "Why were you created"
                        }
                }
        );
        octn_0.setAsChildOf(0, root);

        OfflineConversationTreeNode octn_1 = new OfflineConversationTreeNode(
                "I am Pepper, a humanoid social robot programmed to help you. What else would you like to know?",
                new String[]{
                        "What can you do?",
                        "Who programmed you?",
                        "What is you purpose?"
                },
                new String[][]{
                        new String[]{
                                "What can you do", "Your capabilities", "Your functions", "Your abilities",
                        },
                        new String[]{
                                "Who programmed you", "Who created you", "Who designed your algorithms", "Who wrote your code", "Who developed your intelligence"
                        },
                        new String[]{
                                "What is your purpose",
                                "What is the purpose of your existence",
                                "Why were you created"
                        }
                }
        );
        octn_1.setAsChildOf(1, root);

        OfflineConversationTreeNode octn_2 = new OfflineConversationTreeNode(
                "ITEE stands for the Faculty of Information Technology and Electrical Engineering. What else would you like to know?",
                new String[]{
                        "What is happening at ITEE?",
                        "I want to follow ITEE",
                },
                new String[][]{
                        new String[]{
                                "What is happening at ITEE", "What's new at ITEE", "Any news from ITEE", "Updates from ITEE", "Developments at ITEE", "Whats happening"
                        },
                        new String[]{
                                "I want to follow ITEE", "Follow ITEE", "Follow", "I Wanna follow ITEE"
                        }
                }
        );
        octn_2.setAsChildOf(2, root);

        OfflineConversationTreeNode octn_3 = new OfflineConversationTreeNode(
                "UBICOMP is the Centre for Ubiquitous Computing at the University of Oulu. It is one of the many research groups within the Faculty of ITEE. One of UBICOMP's main research areas is Virtual Reality and Augmented Reality, but we conduct a wide range of research. What else would you like to know?",
                new String[]{
                        "What is happening at UBICOMP?",
                        "I want to follow UBICOMP",
                        "What is Sona?"
                },
                new String[][]{
                        new String[]{
                                "What is happening at UBICOMP", "What's new at UBICOMP", "Any news from UBICOMP", "Updates from UBICOMP", "Developments at UBICOMP"
                        },
                        new String[]{
                                "I want to follow UBICOMP", "Follow UBICOMP", "Follow"
                        },
                        new String[]{
                                "Tell me about Sona", "What is Sona", "Details on Sona please"
                        }
                }
        );
        octn_3.setAsChildOf(3, root);


        OfflineConversationTreeNode octn_4 = new OfflineConversationTreeNode(
                "Sona is the research participant system we use at UBICOMP to invite and schedule participants for our experiments. What else would you like to know?",
                new String[]{
                        "Tell me about the experiments.",
                        "How do I sign up?"
                },
                new String[][]{
                        new String[]{
                                "Tell me about the experiments.", "Experiments", "The experiments"
                        },
                        new String[]{
                                "How do I sign up", "Sign up", "Sign me up", "Register", "I want to sign up"
                        }
                }
        );
        octn_4.setAsChildOf(4, root);


        //        LAYER 2

        OfflineConversationTreeNode octn_0_0 = new OfflineConversationTreeNode(
                "I can help you with information about the ITEE faculty and the UBICOMP research group. We do very exciting research here, would you like to hear about it?",
                new String[]{
                        "Yes, tell me about ITEE",
                        "Yes, tell me about UBICOMP",
                        "No, thanks.",
                },
                new String[][]{
                        new String[]{
                                "Yes, tell me about ITEE", "ITEE", "IT", "ITE", "Tell me about ITE", "Tell me about ITEE"
                        },
                        new String[]{
                                "Yes, tell me about UBICOMP", "UBICOMP", "Tell me about UBICOMP"
                        },
                        new String[]{
                                "No", "Nope", "Its Okay", "No thanks", "No thank you", "I'm Okay"
                        },
                }
        );
        octn_0_0.setAsChildOf(0, octn_0);
        octn_0_0.setAsChildOf(0, octn_1);

        OfflineConversationTreeNode octn_0_1 = new OfflineConversationTreeNode(
                "I was programmed by smart, handsome and very humble researchers here at ITEE."
        );
        octn_0_1.setAsChildOf(1, octn_0);
        octn_0_1.setAsChildOf(1, octn_1);


        OfflineConversationTreeNode octn_0_2 = new OfflineConversationTreeNode(
                "My purpose is to help with Human-Robot Interaction research at UBICOMP and bring joy to the life of the University of Oulu."
        );
        octn_0_2.setAsChildOf(2, octn_0);
        octn_0_2.setAsChildOf(2, octn_1);

        OfflineConversationTreeNode octn_2_0 = new OfflineConversationTreeNode(
                "There is always something interesting happening at ITEE. What would you like to hear about?",
                new String[]{
                        "Facts about ITEE",
                        "Inventions at ITEE",
                        "Research Breakfasts",
                        "Applications to ITEE programmes",
                },
                new String[][]{
                        new String[]{
                                "Facts about ITEE", "Facts"
                        },
                        new String[]{
                                "Inventions at ITEE", "Inventions"
                        }
                        ,
                        new String[]{
                                "ICT Research Breakfasts", "Breakfast", "Breakfasts"
                        },
                        new String[]{
                                "Applications to ITEE programmes", "Applications", "Applications to ITEE programmes"
                        }
                }
        );
        octn_2_0.setAsChildOf(0, octn_2);

        OfflineConversationTreeNode octn_2_1 = new OfflineConversationTreeNode(
                "You can follow us on Twitter at @ITEEOULU and on Instagram at @ITEE_UNIOULU. We regularly share inspiring stories and interesting developments and events at the faculty."
        );
        octn_2_1.setAsChildOf(1, octn_2);

        OfflineConversationTreeNode octn_3_0 = new OfflineConversationTreeNode(
                "Currently we are busy recruiting more participants for our research participation system, Sona. It allows us to invite and manage participants for our experiments. We also share news on our website at ubicomp.oulu.fi and on Twitter at @ubicomp_oulu. One of our most important events, the UBICOMP Summer School is coming up soon, so keep an eye out for photos from there!"
        );
        octn_3_0.setAsChildOf(0, octn_3);

        OfflineConversationTreeNode octn_3_1 = new OfflineConversationTreeNode(
                "We share news on our website at ubicomp.oulu.fi and on Twitter at @ubicomp_oulu. One of our most important events, the UBICOMP Summer School is coming up soon, so keep an eye out for photos from there!"
        );
        octn_3_1.setAsChildOf(1, octn_3);

        octn_4.setAsChildOf(2, octn_3);

        OfflineConversationTreeNode octn_4_0 = new OfflineConversationTreeNode(
                "The available experiments are always changing. One of the currently ongoing studies investigates the accuracy of dropping objects in Virtual Reality. We monitor the participants' heart rate with ECG, their movements with 3D trackers and then they will out a questionnaire."
        );
        octn_4_0.setAsChildOf(0, octn_4);

        OfflineConversationTreeNode octn_4_1 = new OfflineConversationTreeNode(
                "You can create an account at oulu-ubicomp.sona-systems.com. Click Request Account and enter your details, then you will be able to participate in our fun experiments and even get rewards for your help, such as movie tickets, gadgets and university merchandise."
        );
        octn_4_1.setAsChildOf(1, octn_4);


        //        LAYER 3

        OfflineConversationTreeNode octn_2_0_0 = new OfflineConversationTreeNode(
                "ITEE is the faculty with the largest number of personnel among the university's eight faculties. It employs over 600 people, of which 300 are doctoral researchers, and has over 2200 students. ITEE has 12 research units, 4 bachelor and 7 master study programmes. We are also proud to be a truly international faculty with 50 different nationalities and over 50% international employees."
        );
        octn_2_0_0.setAsChildOf(0, octn_2_0);

        OfflineConversationTreeNode octn_2_0_1 = new OfflineConversationTreeNode(
                "Of the total 52 inventions in the University of Oulu in 2022, 26 came from the Faculty of Information and Electrical Engineering. We are happy to be the home of new inventions and excellent research!"
        );
        octn_2_0_1.setAsChildOf(1, octn_2_0);

        OfflineConversationTreeNode octn_2_0_2 = new OfflineConversationTreeNode(
                "Infotech Institute, 6G Flagship, and ITEE Faculty jointly organize a series of ICT Research Breakfasts involving informal and relaxed chats on current research issues. We invite distinguished researchers, experts, and company representatives around different ICT topics to give short presentations followed by a discussion."
        );
        octn_2_0_2.setAsChildOf(2, octn_2_0);

        OfflineConversationTreeNode octn_2_0_3 = new OfflineConversationTreeNode(
                "More than 6400 applications were received for all the Faculty of ITEE degree programmes. In the first joint application period of the spring, there were 7 master's programmes and one bachelor's programme. The second joint application period in the spring had three bachelor's programmes. "
        );
        octn_2_0_3.setAsChildOf(3, octn_2_0);


        octn_2.setAsChildOf(0, octn_0_0);
        octn_3.setAsChildOf(1, octn_0_0);

//        Let's make sure that the tree we created is all good
        root.validate();
    }


    public OfflineConversationTreeNode getCurrent() {
        return current;
    }

    public OfflineConversationTreeNode getRoot() {
        return root;
    }
}
