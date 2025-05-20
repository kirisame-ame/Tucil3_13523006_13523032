"use client";

import * as React from "react";
import { Check, ChevronsUpDown } from "lucide-react";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command";
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover";

import { useAppContext } from "@/hooks/AppProvider";

export default function Combobox({
    param,
    options,
}: {
    param: string;
    options: Map<string, string>;
}) {
    const [open, setOpen] = React.useState(false);
    const [value, setValue] = React.useState("");
    const appContext = useAppContext();

    // Convert Map to arrays for easier rendering
    const displayOptions = Array.from(options.keys());

    // Find the display value for the current selection
    const currentDisplay = value
        ? Array.from(options.entries()).find(([_, v]) => v === value)?.[0] || ""
        : "";

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={open}
                    className="w-[200px] justify-between"
                >
                    {currentDisplay || "Select " + param}
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>
            <PopoverContent className="w-[200px] p-0">
                <Command>
                    <CommandInput
                        placeholder={"Search " + param}
                        className="h-9"
                    />
                    <CommandList>
                        <CommandEmpty>No matching option.</CommandEmpty>
                        <CommandGroup>
                            {displayOptions.map((display) => (
                                <CommandItem
                                    key={display}
                                    value={display}
                                    onSelect={(currentValue) => {
                                        const backendValue =
                                            options.get(currentValue) || "";
                                        setValue(
                                            backendValue === value
                                                ? ""
                                                : backendValue,
                                        );
                                        setOpen(false);
                                        appContext.setState((prevState) => ({
                                            ...prevState,
                                            [param]: backendValue,
                                        }));
                                    }}
                                >
                                    {display}
                                    <Check
                                        className={cn(
                                            "ml-auto",
                                            value === options.get(display)
                                                ? "opacity-100"
                                                : "opacity-0",
                                        )}
                                    />
                                </CommandItem>
                            ))}
                        </CommandGroup>
                    </CommandList>
                </Command>
            </PopoverContent>
        </Popover>
    );
}
