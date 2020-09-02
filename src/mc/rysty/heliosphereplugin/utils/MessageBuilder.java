package mc.rysty.heliosphereplugin.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public class MessageBuilder {

    private List<MessageComponent> messageComponents = new ArrayList<>();

    public MessageComponent append(String append) {
        MessageComponent messageComponent = new MessageComponent(append);
        messageComponents.add(messageComponent);

        return messageComponent;
    }

    public boolean isEmpty() {
        return messageComponents.isEmpty();
    }

    public int getSize() {
        return messageComponents.size();
    }

    public BaseComponent[] build() {
        List<BaseComponent> baseComponents = new ArrayList<>();

        for (MessageComponent messageComponent : messageComponents)
            for (BaseComponent baseComponent : messageComponent.getBaseComponents())
                baseComponents.add(baseComponent);

        return baseComponents.toArray(new BaseComponent[baseComponents.size()]);
    }

    public class MessageComponent {

        private List<BaseComponent> components = new ArrayList<>();

        public MessageComponent(String text) {
            for (BaseComponent component : TextComponent
                    .fromLegacyText(ChatColor.translateAlternateColorCodes('&', text)))
                components.add(component);
        }

        public MessageComponent clickEvent(ClickEvent.Action action, String string) {
            for (BaseComponent component : components)
                component.setClickEvent(new ClickEvent(action, string));
            return this;
        }

        public MessageComponent hoverEvent(HoverEvent.Action action, String string) {
            for (BaseComponent component : components)
                component.setHoverEvent(new HoverEvent(action,
                        TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', string))));
            return this;
        }

        public MessageComponent hoverEvent(HoverEvent.Action action, BaseComponent... input) {
            for (BaseComponent component : components)
                component.setHoverEvent(new HoverEvent(action, input));
            return this;
        }

        private List<BaseComponent> getBaseComponents() {
            return components;
        }
    }
}
