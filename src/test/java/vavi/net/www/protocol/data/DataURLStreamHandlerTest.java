package vavi.net.www.protocol.data;

import java.awt.image.BufferedImage;
import java.net.URI;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class DataURLStreamHandlerTest {

    @Test
    void test1() throws Exception {
        String uri = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAADXElEQVR4XnWSaUxTeRTF6wdj1AlBiTZDH20fxqgfdByLmjgaY1zrMlFpcVgsbtVBR0VRcAMjihLRzGQIIsRlnCnlAVIlIRpA0RAp73Wh6FipliBYuklBRECKfT2+1rgRPcn5dn/nf//3Xh7vCwEYAdRNAO5FAbXRnBXAXWlLfYaSoeTPjOVSa92FHxZ+yXwl663dIUZj3rSEXEvm3GOtptmHW3r3FujfGegrQ90dWVx+DkrOTTo3VSAI+wqki6Ina/9dW0MXr/cYyuRvGiu2VjyrO9r9d3ktOzLegYArTfXwDuSCLlnjuVu4irmfv+heENYVL4toUMkGbOnz/P6ocLDmXTCUx/i77anci6U4oLKASHJh/NYOaO/8hUe3kvxOa9WAvnQTS5LkTzxaHctnimU+zBXCn7carOsQB2aDxSl4vXloaq7GqAQn+NtdyNa0gWV70OW0vGUohS9SGLmE1/Cf1MBQG/x+DvIhi/MJDA5m4rnxINoMOejtrMZohRPiP1xIyHVjoL8dL21an/76tndkBLmAZ7qx+CnrC8ABMAOW+4kwXFeAeXAJygInlIXuYMD0A24IdjqwLd+G3y+YoS3b5ZskFP7CdbC+s6tjJ3y+TBg1Mphr9mGwX4e+XhNOl7digtKFn1PdkKS5MXmPKzhQqqoG+rLNrIgQSbkAWZpR8+urobfHoSuJh+flHbS3UHhhvYYuhwbJVy3BkKnJ7iCsvl0Fd9NJ6Kg4PykQynm0Kj6E20Lf644kLiAWrU8uwtacA7s5Ha7mdPS05yHuz8cI3eJCoaYSXaY02OkUli6Ss2KBMDG4Sq0quvRpTbQ3ENBiOgnnoxS4TUp4Gjei56ES3eZDoDQF8BhSWL1qkeOf/SF1MfPGZYkIIu7DIaljZzBqmVdH/QYrcwSdTXvwqnEz+ozx6NdvRGfDjqFGamV74d6w6uUzJ6YGWicJco5YLA79fI2q1TZGvQFtulPo+T8Drx8ehoNJGaSvLX9+XjHmpjQqLFUcEbGWm7xkBp8/9hP4UQ8uzz9DF8XAbsqFXX/WW5u/1JadGHpj2cyJBwNgJEFM/yb4UfVFK0UNKjk055e2HYvhq2ZN+TGZ++M6kUg0LTw8fMzw+u9pRGCyYoJYwYGkRCIZObxguN4DA+sW29HCoZ8AAAAASUVORK5CYII=";
        BufferedImage image = ImageIO.read(URI.create(uri).toURL());
Debug.println(image);
        assertNotNull(image);
    }
}