package org.limelight.customBoats;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.List;


public class EventListner implements Listener {
    public static double maxDurability;
    private final NamespacedKey durabilityNamespace;
    private final JavaPlugin plugin;

    EventListner(JavaPlugin plugin){
        this.durabilityNamespace = new NamespacedKey(plugin,"durability");
        this.plugin = plugin;
    }



    private Material boatToPlanks(Material type){
        return switch (type) {

            case  Material.OAK_BOAT,  Material.OAK_CHEST_BOAT ->Material.OAK_PLANKS;
            case  Material.ACACIA_BOAT,  Material.ACACIA_CHEST_BOAT -> Material.ACACIA_PLANKS;
            case  Material.SPRUCE_BOAT,  Material.SPRUCE_CHEST_BOAT ->Material.SPRUCE_PLANKS;
            case  Material.CHERRY_BOAT,  Material.CHERRY_CHEST_BOAT ->Material.CHERRY_PLANKS;
            case  Material.DARK_OAK_BOAT, Material.DARK_OAK_CHEST_BOAT ->Material.DARK_OAK_PLANKS;
            case Material.JUNGLE_BOAT,  Material.JUNGLE_CHEST_BOAT ->Material.JUNGLE_PLANKS;
            case Material.MANGROVE_BOAT, Material.MANGROVE_CHEST_BOAT ->Material.MANGROVE_PLANKS;
            case Material.BIRCH_BOAT, Material.BIRCH_CHEST_BOAT -> Material.BIRCH_PLANKS;
            case Material.PALE_OAK_BOAT, Material.PALE_OAK_CHEST_BOAT ->  Material.PALE_OAK_PLANKS;
            case Material.BAMBOO_RAFT ,Material.BAMBOO_CHEST_RAFT -> Material.BAMBOO_PLANKS;
            default ->  Material.AIR;
        };
    }

    private EntityType boatToEntity(Material type){
        return switch (type) {
            case     Material.OAK_BOAT ->EntityType.OAK_BOAT;
            case     Material.ACACIA_BOAT -> EntityType.ACACIA_BOAT;
            case     Material.SPRUCE_BOAT ->EntityType.SPRUCE_BOAT;
            case     Material.CHERRY_BOAT->EntityType.CHERRY_BOAT;
            case     Material.DARK_OAK_BOAT->EntityType.DARK_OAK_BOAT;
            case     Material.JUNGLE_BOAT->EntityType.JUNGLE_BOAT;
            case     Material.MANGROVE_BOAT->EntityType.MANGROVE_BOAT;
            case     Material.BIRCH_BOAT -> EntityType.BIRCH_BOAT;
            case     Material.PALE_OAK_BOAT->  EntityType.PALE_OAK_BOAT;
            case     Material.BAMBOO_RAFT -> EntityType.BAMBOO_RAFT;
            case     Material.OAK_CHEST_BOAT -> EntityType.OAK_CHEST_BOAT;
            case     Material.ACACIA_CHEST_BOAT  -> EntityType.ACACIA_CHEST_BOAT;
            case     Material.SPRUCE_CHEST_BOAT-> EntityType.SPRUCE_CHEST_BOAT;
            case     Material.CHERRY_CHEST_BOAT-> EntityType.CHERRY_CHEST_BOAT;
            case     Material.DARK_OAK_CHEST_BOAT->EntityType.DARK_OAK_CHEST_BOAT;
            case     Material.JUNGLE_CHEST_BOAT ->EntityType.JUNGLE_CHEST_BOAT;
            case     Material.MANGROVE_CHEST_BOAT-> EntityType.MANGROVE_CHEST_BOAT;
            case     Material.BIRCH_CHEST_BOAT-> EntityType.BIRCH_CHEST_BOAT;
            case     Material.PALE_OAK_CHEST_BOAT-> EntityType.PALE_OAK_CHEST_BOAT;
            case     Material.BAMBOO_CHEST_RAFT -> EntityType.BAMBOO_CHEST_RAFT;
            default ->  null;
        };

    }

    private EntityType boatToChestBoat(EntityType name){
        return switch (name) {
            case     EntityType.OAK_BOAT -> EntityType.OAK_CHEST_BOAT;
            case     EntityType.ACACIA_BOAT -> EntityType.ACACIA_CHEST_BOAT;
            case     EntityType.SPRUCE_BOAT -> EntityType.SPRUCE_CHEST_BOAT;
            case     EntityType.CHERRY_BOAT-> EntityType.CHERRY_CHEST_BOAT;
            case     EntityType.DARK_OAK_BOAT->EntityType.DARK_OAK_CHEST_BOAT;
            case     EntityType.JUNGLE_BOAT->EntityType.JUNGLE_CHEST_BOAT;
            case     EntityType.MANGROVE_BOAT->EntityType.MANGROVE_CHEST_BOAT;
            case     EntityType.BIRCH_BOAT->EntityType.BIRCH_CHEST_BOAT;
            case     EntityType.PALE_OAK_BOAT->EntityType.PALE_OAK_CHEST_BOAT;
            case     EntityType.BAMBOO_RAFT-> EntityType.BAMBOO_CHEST_RAFT;
            default ->  EntityType.OAK_BOAT;
        };

    }

    //Drops boat items
    private void dropBoatItems(Location location, Material boatType){
        World world = location.getWorld();
        world.dropItemNaturally(location, new ItemStack(boatToPlanks(boatType),2));
        world.dropItemNaturally(location, new ItemStack(Material.STICK,2));
    }

    //Drops chest boat items
    private void dropChestBoatItems(Location location, Material boatType){
        World world = location.getWorld();
        world.dropItemNaturally(location, new ItemStack(boatToPlanks(boatType),2));
        world.dropItemNaturally(location, new ItemStack(Material.CHEST));
        world.dropItemNaturally(location, new ItemStack(Material.STICK,2));
    }

    //Returns a chat color based on percentage
    private ChatColor percentageToColor(int percentage){
        if (percentage > 66 ) return ChatColor.GREEN;

        else if (33 <= percentage) return ChatColor.YELLOW;

        else return ChatColor.RED;
    }

    //Method in which its input an items type and returns a boolean based if it's a boat or not
    private boolean isBoat(Material type){
        return switch (type) {
            case Material.OAK_BOAT,Material.ACACIA_BOAT,Material.SPRUCE_BOAT,Material.CHERRY_BOAT,Material.DARK_OAK_BOAT,Material.JUNGLE_BOAT,Material.MANGROVE_BOAT,Material.BIRCH_BOAT,Material.PALE_OAK_BOAT, Material.BAMBOO_RAFT -> true;
            default -> false;
        };
    }

    //Method in which its input an items type and returns a boolean based if it's a chest boat or not
    private boolean isChestBoat(Material type){
        return switch (type) {
            case Material.OAK_CHEST_BOAT,Material.ACACIA_CHEST_BOAT,Material.SPRUCE_CHEST_BOAT,Material.CHERRY_CHEST_BOAT,Material.DARK_OAK_CHEST_BOAT,Material.JUNGLE_CHEST_BOAT,Material.MANGROVE_CHEST_BOAT,Material.BIRCH_CHEST_BOAT,Material.PALE_OAK_CHEST_BOAT, Material.BAMBOO_CHEST_RAFT -> true;
            default -> false;
        };
    }

    //Gets a boat's durability stored in its persistent data container
    private boolean isBoatDurability(PersistentDataContainer data){
        return data.has(durabilityNamespace, PersistentDataType.DOUBLE);
    }

    //Saves a boat's durability into its persistent data container
    private void saveBoatDurability(PersistentDataContainer data,double amount){
        data.set(durabilityNamespace, PersistentDataType.DOUBLE,amount);
    }

    //Removes the boat's durability in its persistent data container
    private void removeBoatDurability(PersistentDataContainer data){
        data.remove(durabilityNamespace);
    }

    //Gets the boat's durability from its persistent data container
    private double getBoatDurability(PersistentDataContainer data){
        return data.get(durabilityNamespace, PersistentDataType.DOUBLE);
    }



    // Method in which it adds durability
    @EventHandler
    public void onBoatSpawn(VehicleCreateEvent event){
        if (event.getVehicle() instanceof Boat boat){
            PersistentDataContainer data = boat.getPersistentDataContainer();
            if (!isBoatDurability(data)) saveBoatDurability(data,maxDurability);

        }
    }



    // Method in which the stored persistent data +--is removed from the boat upon its removal to save server storage
    @EventHandler
    public void onBoatBreak(VehicleDestroyEvent event){
        if (event.getVehicle() instanceof Boat boat){
            PersistentDataContainer data = boat.getPersistentDataContainer();
            removeBoatDurability(data);
        }
    }

    //Drops correct boat items if a boat spawns in the world for any reason (like when it breaks)
    @EventHandler
    public void onBoatDropEvent(ItemSpawnEvent event){
        Material type = event.getEntity().getItemStack().getType();

        //If it's a boat drop boat items
        if (isBoat(type)) {
            event.setCancelled(true);
            dropBoatItems(event.getLocation(),type);
        }

        //If it's a chest boat drop boat items + a chest
        else if (isChestBoat(type)){
            event.setCancelled(true);
            dropChestBoatItems(event.getLocation(),type);
        }

    }



    //If a player sneak-rightclick's with a chest it converts a boat into a chestboat
    @EventHandler
    public void onChestBoatClick(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity rightClicked = event.getRightClicked();

        //If the player is sneaking and right-clicked on a boat (NOT A CHEST BOAT)
        if (rightClicked instanceof Boat boat && !(rightClicked instanceof ChestBoat) ){
            if (player.isSneaking()){

                PlayerInventory inventory = player.getInventory();
                ItemStack mainHand = inventory.getItemInMainHand();

                if(mainHand.getType() == Material.CHEST){

                    World world = player.getWorld();
                    Location location = boat.getLocation();
                    PersistentDataContainer data = boat.getPersistentDataContainer();

                    //Spawns a chestBoat version of the boat
                    ChestBoat chestBoat = (ChestBoat) (world.spawnEntity(location, boatToChestBoat(boat.getType())));

                    //Saves the boat durability from the new one to the old one
                    saveBoatDurability(chestBoat.getPersistentDataContainer(),data.get(durabilityNamespace,PersistentDataType.DOUBLE));

                    int amount = mainHand.getAmount()-1;
                    if (amount > 0) inventory.setItemInMainHand(new ItemStack(Material.CHEST,amount));
                    else inventory.setItemInMainHand(null);


                    removeBoatDurability(data);
                    boat.remove();
                }
            }
        }
    }


    @EventHandler
    public void onBoatCraftEvent(CraftItemEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        Material boatType = currentItem.getType();
        if (!(isBoat(boatType) || isChestBoat(boatType))) return;


        Location clickedLocation = event.getClickedInventory().getLocation().clone();
        event.setCancelled(true);

        if (clickedLocation.getBlock().getType() == Material.CRAFTER) return;
        CraftingInventory inventory = event.getInventory();
        ItemStack[] contents = inventory.getMatrix().clone();

        // Copy location safely

        // Async: process inventory modifications
        Bukkit.getAsyncScheduler().runNow(plugin, task -> {
            // Reduce items in the crafting matrix
            for (int i = 0; i < contents.length; i++) {
                ItemStack itemStack = contents[i];
                if (itemStack == null) continue;

                int amount = itemStack.getAmount();
                if (amount <= 1) {
                    contents[i] = null;
                } else {
                    itemStack.setAmount(amount - 1);
                    contents[i] = itemStack;
                }
            }

            // Schedule inventory update on main thread (fast, safe)
            Bukkit.getScheduler().runTask(plugin, () -> inventory.setMatrix(contents));

            // Schedule world modifications on region thread
            Bukkit.getRegionScheduler().run(plugin, clickedLocation, regionTask -> {
                // Adjust spawn location
                clickedLocation.add(0.5, 1, 0.5);

                World world = clickedLocation.getWorld();
                if (world == null) return;

                // Play smithing table sound and spawn the boat
                world.playSound(clickedLocation, Sound.BLOCK_SMITHING_TABLE_USE, 1f, 1f);
                world.spawnEntity(clickedLocation, boatToEntity(boatType));
            });
        });
    }


    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {

        if (event.getVehicle() instanceof Boat boat) {

            List<Entity> passengers = boat.getPassengers();

            Location startLocation = event.getFrom().clone();
            startLocation.setY(0);

            Location endLocation = event.getTo().clone();
            endLocation.setY(0);

            Bukkit.getAsyncScheduler().runNow(plugin, regionTask -> {
                PersistentDataContainer data = boat.getPersistentDataContainer();
                List<Player> playerPassengers = new ArrayList<>();

                passengers.forEach((entity) -> {
                    if (entity instanceof Player player) {
                        playerPassengers.add(player);
                    }
                });

                double durability = getBoatDurability(data) - startLocation.distance(endLocation);
                if (durability > 0) {
                    saveBoatDurability(data, durability);
                    int percentage = (int) ((durability / maxDurability) * (100));
                    String message = percentageToColor(percentage) + "Durability " + (int) Math.round(durability) + "/" + (int) (maxDurability) + " (" + percentage + "%)";

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Player player : playerPassengers) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                        }
                    });

                } else {
                    removeBoatDurability(data);
                    Material boatType = boat.getBoatMaterial();
                    Location location = boat.getLocation();

                    Bukkit.getRegionScheduler().run(plugin, location, task -> {
                        boat.remove();
                        if (boat instanceof ChestBoat) {
                            dropChestBoatItems(location, boatType);
                        } else {
                            dropBoatItems(location, boatType);
                        }

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            for (Player player : playerPassengers) {
                                player.playSound(player, Sound.ITEM_SHIELD_BREAK, 1, 1);
                            }
                        });
                    });
                }
            });
        }
    }


}
