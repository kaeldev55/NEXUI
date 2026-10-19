
package dev.nexui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UIRenderer {

    public void render(Player player, UIProject project) {
        List<Component> renderedComponents = new ArrayList<>();

        for (Object item : project.getComponents()) {
            if (!(item instanceof Map<?, ?>)) {
                continue;
            }

            Map<?, ?> component = (Map<?, ?>) item;

            Object rawVisible = component.get("visible");
            if (Boolean.FALSE.equals(rawVisible)) {
                continue;
            }

            Object rawType = component.get("type");
            if (!"text".equalsIgnoreCase(String.valueOf(rawType))) {
                continue;
            }

            Object rawProperties = component.get("properties");
            if (!(rawProperties instanceof Map<?, ?>)) {
                continue;
            }

            Map<?, ?> properties = (Map<?, ?>) rawProperties;

            Object rawText = properties.get("text");
            String text = rawText == null
                    ? ""
                    : String.valueOf(rawText);

            Object rawColor = properties.get("color");
            String color = rawColor == null
                    ? "#FFFFFF"
                    : String.valueOf(rawColor);

            Component textComponent = Component.text(text);

            TextColor textColor = TextColor.fromHexString(color);
            if (textColor != null) {
                textComponent = textComponent.color(textColor);
            }

            renderedComponents.add(textComponent);
        }

        if (renderedComponents.isEmpty()) {
            player.sendActionBar(
                    Component.text("NEXUI: No text components")
            );
            return;
        }

        Component output = Component.empty();

        for (int i = 0; i < renderedComponents.size(); i++) {
            if (i > 0) {
                TextColor separatorColor =
                        TextColor.fromHexString("#777777");

                Component separator = Component.text(" | ");

                if (separatorColor != null) {
                    separator = separator.color(separatorColor);
                }

                output = output.append(separator);
            }

            output = output.append(renderedComponents.get(i));
        }

        player.sendActionBar(output);
    }
}
