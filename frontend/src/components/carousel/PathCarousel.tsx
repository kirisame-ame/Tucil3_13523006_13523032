import React, { useRef } from "react";
import type { EmblaOptionsType } from "embla-carousel";
import useEmblaCarousel from "embla-carousel-react";
import Autoplay from "embla-carousel-autoplay";
import { useAutoplay } from "./CarouselAutoplay";
import { useAutoplayProgress } from "./CarouselAutoplayProgress";
import {
    NextButton,
    PrevButton,
    usePrevNextButtons,
} from "./CarouselArrowButtons";
import BoardGrid from "../BoardGrid";

type PropType = {
    slides: Array<{
        board: string;
        movement?: {
            piece: string;
            distance: number;
            direction: string;
        };
    }>;
    options?: EmblaOptionsType;
};

const PathCarousel: React.FC<PropType> = (props) => {
    const { slides, options } = props;
    const progressNode = useRef<HTMLDivElement>(null);
    const [emblaRef, emblaApi] = useEmblaCarousel(options, [
        Autoplay({ playOnInit: false, delay: 2000, stopOnInteraction: false }),
    ]);

    const {
        prevBtnDisabled,
        nextBtnDisabled,
        onPrevButtonClick,
        onNextButtonClick,
    } = usePrevNextButtons(emblaApi);

    const { autoplayIsPlaying, toggleAutoplay, onAutoplayButtonClick } =
        useAutoplay(emblaApi);

    const { showAutoplayProgress } = useAutoplayProgress(
        emblaApi,
        progressNode as React.RefObject<HTMLDivElement>,
    );

    return (
        <div className="embla">
            <div className="embla__viewport" ref={emblaRef}>
                <div className="embla__container">
                    {" "}
                    {slides.map((slide, index) => (
                        <div className="embla__slide" key={index}>
                            <div className="flex flex-col items-center gap-4 p-4">
                                <BoardGrid board={slide.board} />
                                {slide.movement && (
                                    <div className="text-white text-center">
                                        <p>
                                            Move piece {slide.movement.piece}{" "}
                                            {slide.movement.distance} steps{" "}
                                            {slide.movement.direction.toLowerCase()}
                                        </p>
                                    </div>
                                )}
                                <div className="text-amber-400">
                                    <span>Step {index + 1}</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="embla__controls">
                <div className="embla__buttons">
                    <PrevButton
                        onClick={() => onAutoplayButtonClick(onPrevButtonClick)}
                        disabled={prevBtnDisabled}
                    />
                    <NextButton
                        onClick={() => onAutoplayButtonClick(onNextButtonClick)}
                        disabled={nextBtnDisabled}
                    />
                </div>

                <div
                    className={`embla__progress`.concat(
                        showAutoplayProgress ? "" : " embla__progress--hidden",
                    )}
                >
                    <div className="embla__progress__bar" ref={progressNode} />
                </div>

                <button
                    className="embla__play"
                    onClick={toggleAutoplay}
                    type="button"
                >
                    {autoplayIsPlaying ? "Stop" : "Start"}
                </button>
            </div>
        </div>
    );
};

export default PathCarousel;
