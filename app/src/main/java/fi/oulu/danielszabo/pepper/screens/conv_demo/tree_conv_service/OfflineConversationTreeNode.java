package fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class OfflineConversationTreeNode {

    //    What Pepper says on this node
    private final String output;
    //    What options Pepper offers on the screen
    private final String[] options;
    //    qi syntax speech recognition phrases mapped to their options by index
    private final String[][] optionPhraseSets;

    //    the node we go to next mapped to the selected option by index
    private final OfflineConversationTreeNode[] children;

    public OfflineConversationTreeNode(String output, String[] options, String[][] optionPhraseSets) {

        if (options == null & optionPhraseSets == null) {
            // ok
            this.children = new OfflineConversationTreeNode[0];
        } else if (options == null & optionPhraseSets != null) {
            throw new RuntimeException("Created node options but not phrase sets!");
        } else if (options != null & optionPhraseSets == null) {
            throw new RuntimeException("Created node has phrase sets but not options!");
        } else if (options.length != optionPhraseSets.length) {
            throw new RuntimeException("Created node has unequal number of options and phrase sets!");
        } else {
            this.children = new OfflineConversationTreeNode[options.length];
        }

        if (options == null) this.options = new String[]{};
        else this.options = options;

        if (optionPhraseSets == null) this.optionPhraseSets = new String[][]{};
        else this.optionPhraseSets = optionPhraseSets;

        this.output = output;
    }

    //    for leaf nodes
    public OfflineConversationTreeNode(String output) {
        this.output = output;
        this.options = new String[]{};
        this.optionPhraseSets = new String[][]{};
        this.children = new OfflineConversationTreeNode[0];
    }

    public void setAsChildOf(int index, OfflineConversationTreeNode parent) {
//        don't try to set this as the child of a leaf
        if (parent.children != null && parent.children[index] != null) {
            throw new RuntimeException("Cannot overwrite child of parent node: " + this);
        }

        parent.children[index] = this;
    }

    public void validate() {
       this.validate(new HashSet<>());
    }

    private void validate(Set<OfflineConversationTreeNode> validated) {
        if(!validated.contains(this)){

            // validate number of children and options
            if (this.children != null &&
                    this.options != null &&
                    this.children.length != this.options.length) {
                throw new RuntimeException("Conversation Tree Validation Error: Node has unequal number of options and children: " + this);
            }


            // validate no punctuation in phrasesets

            if(this.optionPhraseSets != null) {
                for (int i = 0; i < optionPhraseSets.length; i++) {
                    for (int j = 0; j < optionPhraseSets[i].length; j++) {
//                        any special char other than space
                        if(optionPhraseSets[i][j].matches("/[^a-zA-Z0-9]/g")) {
                            throw new RuntimeException("Conversation Tree Validation Error: Phrase has special characters! " + optionPhraseSets[i][j] );
                        }
                    }
                }
            }
        } else {
            validated.add(this);

            for (int i = 0; i < children.length; i++) {
                if (children[i] == null) {
                    throw new RuntimeException("Conversation Tree Validation Error: Node has missing children: " + this);
                }
                children[i].validate(validated);
            }
        }
    }

    public String[][] getOptionPhraseSets() {
        return optionPhraseSets;
    }

    public String getOutput() {
        return output;
    }

    public String[] getOptions() {
        return options;
    }

    public OfflineConversationTreeNode[] getChildren() {
        return children;
    }
}
