package org.dicemc.diceapi.util;

import org.dicemc.diceapi.DiceAPI;
import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PageBuilder {
    private final ArrayList<String> elements;

    private int pages = 0;

    private int elementsPerPage = 5;

    private String title;

    private String titleContainer;

    public PageBuilder(String title, String titleContainerColor) {
        this.elements = new ArrayList<>();
        this.title = title;
        this.titleContainer = titleContainerColor;
    }

    public void append(String element) {
        this.elements.add(element);
        if (this.pages == 0) {
            this.pages++;
            return;
        }
        this.pages = this.elements.size() / this.elementsPerPage + ((this.elements.size() % this.elementsPerPage != 0) ? 1 : 0);
    }

    public int getElementsPerPage() {
        return this.elementsPerPage;
    }

    public boolean hasNoPages() {
        return (this.pages == 0);
    }

    public boolean hasPage(int page) {
        return (page > 0 && page <= this.pages);
    }

    public void remove(String element) {
        this.elements.remove(element);
    }

    public void showPage(Player player, int page) {
        if (!hasPage(page))
            page = 1;
        int start = this.elementsPerPage * (page - 1);
        DiceAPI.getChatAPI().sendPlayerMessageNoHeader(player, this.titleContainer + "====[&f" + this.title + " Page " + page + "/" + this.pages + this.titleContainer + "]====");
        for (int i = start; i < start + this.elementsPerPage &&
                i + 1 <= this.elements.size(); i++)
            DiceAPI.getChatAPI().sendPlayerMessageNoHeader(player, this.elements.get(i));
    }

    public void showPage(CommandSender sender, int page) {
        if (sender instanceof Player) {
            showPage((Player)sender, page);
            return;
        }
        if (!hasPage(page))
            page = 1;
        int start = this.elementsPerPage * (page - 1);
        DiceAPI.getChatAPI().outNoHeader(this.titleContainer + "====[&f" + this.title + " Page " + page + "/" + this.pages + this.titleContainer + "]====");
        for (int i = start; i < start + this.elementsPerPage &&
                i + 1 <= this.elements.size(); i++)
            DiceAPI.getChatAPI().outNoHeader(this.elements.get(i));
    }

    public void setElementsPerPage(int elements) {
        this.elementsPerPage = elements;
    }

    public void setTitleContainerColor(String color) {
        this.titleContainer = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
