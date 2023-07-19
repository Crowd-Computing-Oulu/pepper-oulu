package fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.offline_service;

import android.content.Context;
import android.content.res.Resources;

import java.util.Arrays;

import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.log.LOG;
import lombok.Getter;

@Getter
public class OfflineConversationTree {

    private final OfflineConversationTreeNode root;
    private OfflineConversationTreeNode current;
    private Context context;

    public OfflineConversationTreeNode selectChild(int childIndex) {
        // This WILL throw index out of bounds exception
        // if we're going crazy selecting children mindlessly
        return current = current.getChildren()[childIndex];
    }

    public OfflineConversationTreeNode input(String input) {
        // Let's find which child corresponds to this input
        for (int i = 0; i < current.getOptions().length; i++) {
            // This WILL throw an index out of bounds exception
            // if we're going crazy selecting children mindlessly

            // Let's check if the input we received is the displayed option itself
            if (current.getOptions()[i].equalsIgnoreCase(input)) {
                // Yay, it is!
                LOG.debug(this, "Matched option: " + current.getOptions()[i]);
                return current = current.getChildren()[i];
            } else {
                // Well, it wasn't. Now let's check if the input is one of the phrases for this option
                for (int j = 0; j < current.getOptionPhraseSets()[i].length; j++) {
                    if (input.equalsIgnoreCase(current.getOptionPhraseSets()[i][j])) {
                        // Yep!
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

        // We are already on a leaf or the input is not valid on this node.
        // Let's return null and decide outside how we handle it
        return null;
    }

    // Now let's build the whole tree.
    public OfflineConversationTree(Context context) {
        this.context = context;

        // LAYER 1
        current = root = new OfflineConversationTreeNode(
                context.getString(R.string.pepper_intro),
                context.getResources().getStringArray(R.array.node_1_options),
                new String[][]{
                        context.getResources().getStringArray(R.array.node_1_option_phrases_1),
                        context.getResources().getStringArray(R.array.node_1_option_phrases_2),
                        context.getResources().getStringArray(R.array.node_1_option_phrases_3),
                        context.getResources().getStringArray(R.array.node_1_option_phrases_4),
                        context.getResources().getStringArray(R.array.node_1_option_phrases_5)
                }
        );

        OfflineConversationTreeNode octn_0 = new OfflineConversationTreeNode(
                context.getString(R.string.pepper_intro2),
                context.getResources().getStringArray(R.array.questions),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases),
                        context.getResources().getStringArray(R.array.question_2_phrases),
                        context.getResources().getStringArray(R.array.question_3_phrases)
                }
        );
        octn_0.setAsChildOf(0, root);
        octn_0.setAsChildOf(1, root);
        octn_0.setAsChildOf(2, root);

        OfflineConversationTreeNode octn_1 = new OfflineConversationTreeNode(
                context.getString(R.string.pepper_intro3),
                context.getResources().getStringArray(R.array.questions_octn_1),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases_octn_1),
                        context.getResources().getStringArray(R.array.question_2_phrases_octn_1),
                        context.getResources().getStringArray(R.array.question_3_phrases_octn_1)
                }
        );
        octn_1.setAsChildOf(1, root);

        OfflineConversationTreeNode octn_2 = new OfflineConversationTreeNode(
                context.getString(R.string.itee_intro),
                context.getResources().getStringArray(R.array.questions_octn_2),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases_octn_2),
                        context.getResources().getStringArray(R.array.question_2_phrases_octn_2)
                }
        );
        octn_2.setAsChildOf(2, root);

        OfflineConversationTreeNode octn_3 = new OfflineConversationTreeNode(
                context.getString(R.string.ubicomp_intro),
                context.getResources().getStringArray(R.array.questions_octn_3),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases_octn_3),
                        context.getResources().getStringArray(R.array.question_2_phrases_octn_3),
                        context.getResources().getStringArray(R.array.question_3_phrases_octn_3)
                }
        );
        octn_3.setAsChildOf(3, root);

        OfflineConversationTreeNode octn_4 = new OfflineConversationTreeNode(
                context.getString(R.string.sona_intro),
                context.getResources().getStringArray(R.array.questions_octn_4),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases_octn_4),
                        context.getResources().getStringArray(R.array.question_2_phrases_octn_4)
                }
        );
        octn_4.setAsChildOf(4, root);

        // LAYER 2

        OfflineConversationTreeNode octn_0_0 = new OfflineConversationTreeNode(
                context.getString(R.string.exciting_research_info),
                context.getResources().getStringArray(R.array.questions_octn_0_0),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases_octn_0_0),
                        context.getResources().getStringArray(R.array.question_2_phrases_octn_0_0),
                        context.getResources().getStringArray(R.array.question_3_phrases_octn_0_0)
                }
        );
        octn_0_0.setAsChildOf(0, octn_0);
        octn_0_0.setAsChildOf(0, octn_1);

        OfflineConversationTreeNode octn_0_1 = new OfflineConversationTreeNode(context.getString(R.string.research_assistant_info));
        octn_0_1.setAsChildOf(1, octn_0);
        octn_0_1.setAsChildOf(1, octn_1);

        OfflineConversationTreeNode octn_0_2 = new OfflineConversationTreeNode(context.getString(R.string.purpose_info));
        octn_0_2.setAsChildOf(2, octn_0);
        octn_0_2.setAsChildOf(2, octn_1);

        OfflineConversationTreeNode octn_2_0 = new OfflineConversationTreeNode(
                context.getString(R.string.interesting_itee_info),
                context.getResources().getStringArray(R.array.questions_octn_2_0),
                new String[][]{
                        context.getResources().getStringArray(R.array.question_1_phrases_octn_2_0),
                        context.getResources().getStringArray(R.array.question_2_phrases_octn_2_0),
                        context.getResources().getStringArray(R.array.question_3_phrases_octn_2_0),
                        context.getResources().getStringArray(R.array.question_4_phrases_octn_2_0)
                }
        );
        octn_2_0.setAsChildOf(0, octn_2);

        OfflineConversationTreeNode octn_2_1 = new OfflineConversationTreeNode(context.getString(R.string.follow_us_info));
        octn_2_1.setAsChildOf(1, octn_2);

        OfflineConversationTreeNode octn_3_0 = new OfflineConversationTreeNode(context.getString(R.string.recruiting_participants_info));
        octn_3_0.setAsChildOf(0, octn_3);

        OfflineConversationTreeNode octn_3_1 = new OfflineConversationTreeNode(context.getString(R.string.current_experiments_info));
        octn_3_1.setAsChildOf(1, octn_3);

        octn_4.setAsChildOf(2, octn_3);

        OfflineConversationTreeNode octn_4_0 = new OfflineConversationTreeNode(context.getString(R.string.current_experiments_info));
        octn_4_0.setAsChildOf(0, octn_4);

        OfflineConversationTreeNode octn_4_1 = new OfflineConversationTreeNode(context.getString(R.string.sign_up_info));
        octn_4_1.setAsChildOf(1, octn_4);

        // LAYER 3

        OfflineConversationTreeNode octn_2_0_0 = new OfflineConversationTreeNode(context.getString(R.string.itee_stats_info));
        octn_2_0_0.setAsChildOf(0, octn_2_0);

        OfflineConversationTreeNode octn_2_0_1 = new OfflineConversationTreeNode(context.getString(R.string.inventions_info));
        octn_2_0_1.setAsChildOf(1, octn_2_0);

        OfflineConversationTreeNode octn_2_0_2 = new OfflineConversationTreeNode(context.getString(R.string.research_breakfasts_info));
        octn_2_0_2.setAsChildOf(2, octn_2_0);

        OfflineConversationTreeNode octn_2_0_3 = new OfflineConversationTreeNode(context.getString(R.string.applications_info));
        octn_2_0_3.setAsChildOf(3, octn_2_0);

        octn_2.setAsChildOf(0, octn_0_0);
        octn_3.setAsChildOf(1, octn_0_0);
        root.setAsChildOf(2, octn_0_0);

        // Let's make sure that the tree we created is all good
        root.validate();
    }
}
