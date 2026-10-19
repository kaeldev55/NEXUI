
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
            if (!(item instanceof Map<?, ?> component)) {
                continue;
            }

            if (Boolean.FALSE.equals(component.get("visible"))) {
                continue;
            }

            if (!"text".equalsIgnoreCase(
                    String.valueOf(component.get("type")))) {
                continue;
            }

            Object rawProperties = component.get("properties");

            if (!(rawProperties instanceof Map<?, ?> properties)) {
                continue;
            }

            String text = String.valueOf(
                    properties.getOrDefault("text", "")
            );

            String color = String.valueOf(
                    properties.getOrDefault("color", "#FFFFFF")
            );

            Component textComponent = Component.text(text);
            TextColor textColor = TextColor.fromHexString(color);

            if (textColor != null) {
                textComponent = textComponent.color(textColor);
            }

            renderedComponents.add(textComponent);
        }

        if (renderedComponents.isEmpty()) {
            player.sendActionBar(Component.text("NEXUI: No text components"));
            return;
        }

        Component output = Component.empty();

        for (int i = 0; i < renderedComponents.size(); i++) {
            if (i > 0) {
                output = output.append(
                        Component.text(" | ")
                                .color(TextColor.fromHexString("#777777"))
                );
            }

            output = output.append(renderedComponents.get(i));
        }

        player.sendActionBar(output);
    }
}
