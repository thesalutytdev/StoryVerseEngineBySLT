package org.thesalutyt.storyverse.api.camera.cutscene;

public class CutsceneArguments {
    public double x;
    public double y;
    public double z;
    public double rotX;
    public double rotY;
    public CutsceneType type;

    public CutsceneArguments(CutsceneType type, double[] pos, Object[] args) {
        switch (type) {
            case FULL:
            case ROT_ONLY:
            case MOVING:
                this.type = type;
                this.x = pos[0];
                this.y = pos[1];
                this.z = pos[2];

                this.rotX = (double) args[1];
                this.rotY = (double) args[0];

                break;

            case POS_ONLY:
                this.type = type;
                this.x = pos[0];
                this.y = pos[1];
                this.z = pos[2];

                break;
            case NULL:
                this.type = type;
                break;
            default:
                break;
        }
    }
}
