package kr.syeyoung.dungeonsguide.roomedit.mechanicedit;

import kr.syeyoung.dungeonsguide.dungeon.data.OffsetPoint;
import kr.syeyoung.dungeonsguide.dungeon.mechanics.DungeonSecret;
import kr.syeyoung.dungeonsguide.roomedit.EditingContext;
import kr.syeyoung.dungeonsguide.roomedit.MPanel;
import kr.syeyoung.dungeonsguide.roomedit.Parameter;
import kr.syeyoung.dungeonsguide.roomedit.elements.*;
import kr.syeyoung.dungeonsguide.roomedit.valueedit.ValueEdit;
import kr.syeyoung.dungeonsguide.roomedit.valueedit.ValueEditCreator;
import kr.syeyoung.dungeonsguide.utils.TextUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class ValueEditSecret extends MPanel implements ValueEdit<DungeonSecret> {
    private Parameter parameter;

    // scroll pane
    // just create
    // add set
    private DungeonSecret dungeonSecret;

    private MLabel label;
    private MValue<OffsetPoint> value;
    private MStringSelectionButton selectionButton;
    private MTextField preRequisite;
    private MLabelAndElement preRequisite2;

    public ValueEditSecret(final Parameter parameter2) {
        this.parameter = parameter2;
        this.dungeonSecret = (DungeonSecret) parameter2.getNewData();


        label = new MLabel();
        label.setText("Secret Point");
        label.setAlignment(MLabel.Alignment.LEFT);
        add(label);

        value = new MValue(dungeonSecret.getSecretPoint(), Collections.emptyList());
        add(value);

        selectionButton = new MStringSelectionButton(Arrays.asList(new String[] {"CHEST", "BAT", "ITEM_DROP"}), "CHEST");
        selectionButton.setOnUpdate(new Runnable() {
            @Override
            public void run() {
                dungeonSecret.setSecretType(DungeonSecret.SecretType.valueOf(selectionButton.getSelected()));
            }
        });
        add(selectionButton);

        preRequisite = new MTextField() {
            @Override
            public void edit(String str) {
                dungeonSecret.setPreRequisite(Arrays.asList(str.split(",")));
            }
        };
        preRequisite.setText(TextUtils.join(dungeonSecret.getPreRequisite(), ","));
        preRequisite2 = new MLabelAndElement("Req.",preRequisite);
        preRequisite2.setBounds(new Rectangle(0,60,getBounds().width,20));
        add(preRequisite2);
    }

    @Override
    public void onBoundsUpdate() {
        label.setBounds(new Rectangle(0,0,getBounds().width, 20));
        value.setBounds(new Rectangle(0,20,getBounds().width, 20));
        selectionButton.setBounds(new Rectangle(0,40,getBounds().width, 20));
        preRequisite2.setBounds(new Rectangle(0,60,getBounds().width,20));
    }

    @Override
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public void renderWorld(float partialTicks) {
        dungeonSecret.highlight(new Color(0,255,0,50), parameter.getName(), EditingContext.getEditingContext().getRoom(), partialTicks);
    }

    @Override
    public void resize(int parentWidth, int parentHeight) {
        this.setBounds(new Rectangle(0,0,parentWidth, parentHeight));
    }

    public static class Generator implements ValueEditCreator<ValueEditSecret> {

        @Override
        public ValueEditSecret createValueEdit(Parameter parameter) {
            return new ValueEditSecret(parameter);
        }

        @Override
        public Object createDefaultValue(Parameter parameter) {
            return new DungeonSecret();
        }

        @Override
        public Object cloneObj(Object object) {
            try {
                return ((DungeonSecret)object).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            assert false;
            return null;
        }
    }
}
