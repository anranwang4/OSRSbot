package scripts;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

@Script.Manifest(name="Hello World!", description="How you doing!", properties="author=Anran; topic=999; client=4;")

public class HelloWorld extends PollingScript<ClientContext> {

    @Override
    public void start() {
        System.out.println("Started.");
    }

    @Override
    public void stop() {
        System.out.println("Stopped.");
    }

    @Override
    public void poll() {
        // constant loop
        System.out.println("Running.");
    }
}
